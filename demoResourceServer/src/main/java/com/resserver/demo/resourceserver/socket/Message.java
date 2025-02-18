package com.resserver.demo.resourceserver.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String from;
    private String text;
    private String to; // ✅ For private messaging (null for broadcast)
}

