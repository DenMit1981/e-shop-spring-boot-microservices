package com.denmit.eshop.historyservice.client;

import com.denmit.eshop.historyservice.dto.ProductResponseDto;
import com.denmit.eshop.historyservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods-service", path = "/api/v1/goods")
@SecuredFeignClient
public interface ProductClient {

    @GetMapping("/units/{productId}/history")
    ProductResponseDto getUnitForHistoryById(@PathVariable("productId") Long productId);
}
