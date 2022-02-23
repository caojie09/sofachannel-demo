package io.sofastack.dynamic.provider;

import io.sofastack.dynamic.facade.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * index controller
 *
 * @author caojie.cj@antfin.com
 * @since 2020/2/11
 */
@RestController
public class IndexController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/dynamicProvider")
    public String index() {
        return "hello index";
    }


}
