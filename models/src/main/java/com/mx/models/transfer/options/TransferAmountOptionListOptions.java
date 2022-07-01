package com.mx.models.transfer.options;

import lombok.Data;

/**
 * Used for encapsulating the request parameters for a TransferAmountOption#list request.
 *
 * https://developer.mx.com/drafts/mdx/transfer/index.html#transfer-amount-options
 */
@Data
public class TransferAmountOptionListOptions {
  private String transferType;
  private String sourceAccountId;
  private String destinationAccountId;
}
