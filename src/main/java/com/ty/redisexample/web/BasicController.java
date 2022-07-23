package com.ty.redisexample.web;

import com.ty.redisexample.model.Event;
import com.ty.redisexample.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
//Example class
public class BasicController {

  private final EventService eventService;

  @GetMapping("api/events")
  public Flux<Event> listEvents() {
    return eventService.listEvents();
  }

}
