package com.ty.redisexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Example class
public class Event implements Serializable {
  private UUID id;
  private String message;
  private LocalDate time;
}
