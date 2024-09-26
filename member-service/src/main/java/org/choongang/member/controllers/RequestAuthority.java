package org.choongang.member.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAuthority {
    private long memberSeq;
    private String authorityName;
    private boolean invoke;


}
