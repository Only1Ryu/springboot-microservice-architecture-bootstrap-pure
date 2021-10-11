package com.app.eventrade.roleservice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

	@JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timeStamp;
	private Integer statusCode;
	private String response;
}
