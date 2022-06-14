package com.mx.accessors.document;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.documents.Document;
import com.mx.models.documents.DocumentSearch;

/**
 * Accessor base for document operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/documents/#mdx-documents">Specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/documents/#mdx-documents")
public abstract class DocumentBaseAccessor extends Accessor {

  public DocumentBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a document
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a document")
  public AccessorResponse<Document> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List documents
   * @param documentSearch
   * @return
   */
  @GatewayAPI
  @API(description = "List all documents")
  public AccessorResponse<MdxList<Document>> list(DocumentSearch documentSearch) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
