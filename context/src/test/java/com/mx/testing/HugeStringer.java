package com.mx.testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.Data;

@Data
public class HugeStringer {
  private String data;

  public HugeStringer() throws IOException {
    data = new String(Files.readAllBytes(Paths.get("./src/test/resources/huge.txt")));
  }
}
