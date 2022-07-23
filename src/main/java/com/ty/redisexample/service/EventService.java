package com.ty.redisexample.service;

import com.github.javafaker.Faker;
import com.ty.redisexample.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
//Example class
public class EventService {

  private final Faker faker = new Faker();

  private final ReactiveRedisTemplate<String, Event> redisTemplate;

  //Important method
  public Flux<Event> listEvents() {
    //code without loggers
    Flux.fromStream(IntStream.range(0, faker.number().numberBetween(1, 10)).mapToObj(x -> getEvent()))
        .flatMap(x -> redisTemplate.opsForValue().set("name", x));

    //code with loggers
    return Flux.fromStream(IntStream.range(0, faker.number().numberBetween(1, 10)).mapToObj(x -> getEvent()))
        //.flatMap(x -> redisTemplate.opsForHash().get("key", "name")
        .flatMap(x -> redisTemplate.opsForValue().get("name")
            .defaultIfEmpty(Event.builder().build())
            .map(prev -> {
              log.info("Previous value: {}", prev);
              return x;
            })
            //.flatMap(t -> redisTemplate.opsForHash().put("key", "name", x))
            .flatMap(t -> redisTemplate.opsForValue().set("name", x))

            .map(result -> {
              log.info("Write result: {}", result);
              return x;
            }));
  }

  private Event getEvent() {
    return Event.builder()
        .id(UUID.randomUUID())
        .message(faker.gameOfThrones().character() + " born in " + faker.gameOfThrones().city() + " and belongs to " + faker.gameOfThrones().house() + " house")
        .time(Instant.ofEpochMilli(faker.date().birthday().getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate())
        .build();
  }

}
