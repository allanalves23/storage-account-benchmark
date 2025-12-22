package com.testdiskperf.demo.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para o contrato:
 * {"id":1,"first_name":"Laird","last_name":"Baunton","email":"lbaunton0@tiny.cc","gender":"Male","ip_address":"220.73.101.107"}
 */
public record DataMockDTO(
    @JsonProperty("id") Long id,
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    @JsonProperty("email") String email,
    @JsonProperty("gender") String gender,
    @JsonProperty("ip_address") String ipAddress
) {}