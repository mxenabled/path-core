package com.mx.accessors;

@API(notes = "Default status operations.")
public class StatusDefaultAccessor extends StatusBaseAccessor {
  public StatusDefaultAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  @API(notes = "Does no health check, just returns 204")
  public final AccessorResponse<Void> get() {
    return new AccessorResponse<Void>().withStatus(AccessorResponseStatus.NO_CONTENT);
  }
}
