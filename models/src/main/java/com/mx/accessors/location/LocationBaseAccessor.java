package com.mx.accessors.location;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.location.Location;
import com.mx.models.location.LocationSearch;

/**
 * Accessor base for location operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/location/#mdx-location">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/location/#mdx-location")
public abstract class LocationBaseAccessor extends Accessor {

  public LocationBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a location
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a location")
  public AccessorResponse<Location> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Search for locations using {@link LocationSearch}
   * @param locationSearch
   * @return
   */
  @GatewayAPI
  @API(description = "Search for locations using LocationSearch")
  public AccessorResponse<MdxList<Location>> search(LocationSearch locationSearch) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
