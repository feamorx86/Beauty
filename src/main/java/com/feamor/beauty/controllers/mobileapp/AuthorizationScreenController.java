package com.feamor.beauty.controllers.mobileapp;

import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.managers.Constants;

/**
 * Created by Home on 01.09.2016.
 */
public class AuthorizationScreenController extends BaseController {

    @Override
    public int controllerId() {
        return Constants.Controllers.MOBILE_AUTHORIZATION_SCREEN;
    }

    /**
     * mapped as /mobile/auth
     * actions:
     *      action=login, &type=login_and_password |??? &p_login=?, p_password=?, p_???=??? -> {status: success | error, userid: id | error:code, error_descr:}
     *      action=logout -> {status:success |error, error:code, error_descr: }
     *      action=register &type=login_and_password &p_login=?, &p_? = ? -> {status:success | error, userid:id, error:code, error_descr:descr}
     *      action=recover &type=login and email... ->password to email.
     *      action=drop_password &code=code_from_email -> new password
     *      action=remove
     *      action=recover_removed
     */

}
