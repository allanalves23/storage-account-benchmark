package com.testdiskperf.demo.shared;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LargeDataMockDTO(
    @JsonProperty("_id") String id,
    @JsonProperty("index") Integer index,
    @JsonProperty("guid") String guid,
    @JsonProperty("isActive") Boolean isActive,
    @JsonProperty("balance") String balance,
    @JsonProperty("picture") String picture,
    @JsonProperty("age") Integer age,
    @JsonProperty("eyeColor") String eyeColor,
    @JsonProperty("name") String name,
    @JsonProperty("gender") String gender,
    @JsonProperty("company") String company,
    @JsonProperty("email") String email,
    @JsonProperty("phone") String phone,
    @JsonProperty("address") String address,
    @JsonProperty("about") String about,
    @JsonProperty("registered") String registered,
    @JsonProperty("latitude") Double latitude,
    @JsonProperty("longitude") Double longitude,
    @JsonProperty("tags") List<String> tags,
    @JsonProperty("friends") List<FriendDto> friends,
    @JsonProperty("greeting") String greeting,
    @JsonProperty("favoriteFruit") String favoriteFruit
) {
    public record FriendDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name
    ) {}
}
