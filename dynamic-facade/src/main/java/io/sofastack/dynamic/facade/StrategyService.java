package io.sofastack.dynamic.facade;

import io.sofastack.dynamic.model.ProductInfo;

import java.util.List;

/**
 * 对传入的商品列表进行排序并返回
 *
 * @author caojie.cj@antfin.com
 * @since 2020/2/11
 */
public interface StrategyService {
    List<ProductInfo> strategy(List<ProductInfo> products);
}
