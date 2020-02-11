package io.sofastack.dynamic.provider.impl;

import com.alipay.sofa.runtime.api.annotation.SofaService;
import io.sofastack.dynamic.facade.StrategyService;
import io.sofastack.dynamic.model.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对传入的商品列表进行排序实现类
 *
 * @author caojie.cj@antfin.com
 * @since 2020/2/11
 */
@Service
@SofaService
public class StrategyServiceImpl implements StrategyService {
    @Override
    public List<ProductInfo> strategy(List<ProductInfo> products) {
        return products;
    }
}
