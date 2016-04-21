package com.feamor.beauty.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Home on 27.02.2016.
 */
@RestController
@Transactional
public class SimplePageController {
    void a(){
        Page<Page> p;
        
    }
}
