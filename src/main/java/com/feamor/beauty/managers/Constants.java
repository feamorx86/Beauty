package com.feamor.beauty.managers;

import org.eclipse.jetty.util.IO;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * Created by Home on 09.05.2016.
 */
public class Constants {
    public static class GroupDataType {
        public static final int MENU_ITEM = 1500;
        public static final int CONTROLLERS_MAPPING = 1600;
        public static final int LOCALIZATION = 1700;
        public static final int TEMPLATE_EDITOR_MENU_JSON = 5000;
        public static final int TEMPLATE_EDITOR_LABELS_JSON = 5001;

        public static final int HOME_MENU = 5100;
        public static final int HOME_MENU_TITLE = 5101;
        public static final int HOME_MENU_URL = 5102;

        public static final int COMPANY_INFO = 5200;
        public static class CompanyInfoFieldTypes {
            public static final int ShortName = 0;
            public static final int MainEmail = 1;
            public static final int MainPhone = 2;
        }

        public static final int MOBILE_START_DATA_ROOT = 5300;
        public static class MobileStartAppDataTypes {
            public static final int SPLASH_BACKGROUND_IMAGE_URL = 5301;
            public static final int SPLASH_BACKGROUND_TYPE = 5302;

            public static final int SPLASH_TITLE_TEXT = 5310;
            public static final int SPLASH_TITLE_TEXT_SETTINGS = 5311;
            public static final int SPLASH_MESSAGE = 5312;
            public static final int SPLASH_MESSAGE_SETTINGS = 5313;

            public static final int SERVER_VERSION = 5320;
            public static final int UPDATE_URL = 5321;
        }

        public static final int MOBILE_COMPONENTS_ROOT = 5500;

        public static class MobileAppCommonComponents {
            public static final int CLIENT_MAIN_MENU = 5510;
            public static final int EMPLOYER_MAIN_MENU = 5511;
            public static final int ADMIN_MAIN_MENU = 5512;
            public static final int DIRECTOR_MAIN_MENU = 5513;

            public static final int CLIENT_NEWS_SCREEN = 5520;
            public static final int EMPLOYER_NEWS_SCREEN = 5521;
            public static final int ADMIN_NEWS_MANAGER = 5522;
            public static final int ADMIN_NEWS_SCREEN = 5523;
            public static final int DIRECTOR_NEWS_SCREEN = 5524;

            public static final int CLIENT_CONTACTS_SCREEN = 5530;
            public static final int ADMIN_CONTACTS_MANAGER = 5531;

            public static final int CLIENT_MAIN_CATALOG = 5540;
            public static final int ADMIN_MAIN_MANAGER = 5541;

            public static final int CLIENT_EMPLOYERS_SCREEN = 5550;
            public static final int ADMIN_EMPLOYERS_MANAGER = 5551;
            public static final int DIRECTOR_EMPLOYERS_SCREEN = 5552;

            public static final int CLIENT_ACCOUNT_SCREEN = 5560;
            public static final int EMPLOYER_ACCOUNT_SCREEN = 5561;
            public static final int DIRECTOR_ACCOUNT_SCREEN = 5562;
            public static final int ADMIN_ACCOUNT_SCREEN = 5563;

            public static final int CLIENT_BEAST_WORKS_SCREEN = 5570;
            public static final int EMPLOYER_BEAST_WORKS_MANAGER = 5571;

            public static final int CLIENT_SCHEDULE_SCREEN = 5580;
            public static final int EMPLOYER_SCHEDULE_SCREEN = 5581;
            public static final int ADMIN_SCHEDULE_SCREEN = 5582;
        }

        public static final int MOBILE_APP_TYPES = 5700;
        public static final int MOBILE_APP_CLIENT_COMPONENTS_ROOT = 5710;
        public static final int MOBILE_APP_EMPLOYER_COMPONENTS_ROOT = 5711;
        public static final int MOBILE_APP_DIRECTOR_COMPONENTS_ROOT = 5712;
        public static final int MOBILE_APP_ADMIN_COMPONENTS_ROOT = 5713;

        public static class MobileAppMenuSetup {
            public static final int TYPE_TYPES = 5800;
            public static final int TYPE_STYLE = 5801;
            public static final int TYPE_ITEM = 5802;

            public static final int TYPE_TYPES_ROOT = 5803;
            public static final int TYPE_STYLES_ROOT = 5804;
        }

        public static class MobileAppMenuItemTypes {
            public static final int TYPE_HEADER_LABEL = 5811;
            public static final int TYPE_HEADER_IMAGE = 5812;
            public static final int TYPE_HEADER_COMPANY = 5813;
            public static final int TYPE_HEADER_CLIENT = 5814;

            public static final int TYPE_ITEM_TEXT = 5830;
            public static final int TYPE_ITEM_ICON_TEXT = 5831;
            public static final int TYPE_ITEM_IMAGE = 5842;

            public static final int TYPE_DELIMITER_LINE = 5850;
            public static final int TYPE_DELIMITER_SPACE = 5851;
        }

    }

    public static class Controllers {
        public static final int BASE = 0;
        public static final int HOME_PAGE_CONTROLLER = 1;
        public static final int TEST_CONTROLLER = 2;
        public static final int TEMPLATE_EDITOR = 3;
        public static final int MAPPINGS_EDITOR = 4;
        public static final int SHARE_VIEW = 5;
        public static final int BASE_ERROR_HANDLER = 6;
        public static final int NEWS_LIST_CONTROLLER = 7;

        public static final int MOBILE_APP_START_DATA = 8;
        public static final int MOBILE_AUTHORIZATION_SCREEN = 9;
        public static final int MOBILE_MAIN_SCREEN = 10;
        public static final int MOBILE_MAIN_MENU = 11;
        public static final int MOBILE_NEWS = 12;

    }

    public static class Blocks {
        public static final int BASE_BLOCK = 0;
        public static class Header {
            public static final int SIMPLE_BLOCK = 20000;
            public static final int SIMPLE_MENU_BLOCK = 20200;
            public static final int SIMPLE_MENU_BLOCK_ITEM = 20400;
        }

        public static class Footer {
            public static final int FOOTER = 51150;
            public static final int FOOTER_ABOUT = 51151;
        }

        public static class Common {
            public static final int MENU = 51110;
            public static final int MENU_ITEM = 51111;
            public static final int PAGE = 51100;

            public static final int TEXT_AND_URL = 51201;
            public static final int BASE_CONTAINER = 51202;
            public static final int BASE_PAGE_WITH_CONTENT = 51203;
            public static final int BASE_PAGE_WITH_HEADER_CONTENT_FOOTER = 51204;
        }

        public static class Content {
            public static final int SIMPLE_LIST_BLOCK = 50000;
            public static final int SIMPLE_CONTAINER =  50001;
            public static final int SIMPLE_CONTAINER_WITH_HEADER =  50002;
            public static class TemplateEditor {
                public static final int PAGE = 50100;
                public static final int ERROR_BLOCK = 50101;
                public static final int LIST_ITEM = 50102;
                public static final int MENU_ITEM = 50103;
                public static final int VIEWER_BLOCK = 50104;
                public static final int LIST_BLOCK = 50105;
            }

            public static class Home {
                public static final int CONTENT_ITEM = 51112;
            }

            public static class News {
                public static final int NEWS_BLOCK_ITEM = 51200;

                public static final int LIST_PAGE = 51201;
                public static final int ITEM_PAGE = 51202;
                public static final int MIDDLE_IMAGE_NEWS_ITEM = 51203;

                public static final int LIST_CONTENT_ITEM = 51220;
                public static final int ITEM_CONTENT_ITEM = 51230;

                public static final int LIST_PAGES = 51220;
            }
        }

        public static class ContentBlock {

            public static final int NEWS_SIMPLE_BLOCK = 100000;
            public static final int NEWS_ITEM_SIMPLE = 100010;

            public static final int PROJECTS_SIMPLE_BLOCK = 100000;
            public static final int SIMPLE_NEWS_ITEM = 100010;

            public static final int SIMPLE_TEXT_CONTROL = 100020;
        }
    }

    public static class RenderData {
        public static final int COMPANY_INFO = 0;
        public static final int MENU = 1;
        public static final int NEWS_SUMMARU_LIST = 2;
    }

    public static class Pages {
        public static final int BASE_PAGE = 0;
        public static final int BASE_PAGE_WITH_TEMPLATE = 10;
        public static final int TEST_PAGE_TYPE = 100;
        public static final int TEMPLATE_EDITOR_VIEW_TEMPLATE = 120;
        public static final int TEMPLATE_EDITOR_LIST_TEMPLATES = 121;

        public static final int NEWS_CREATED_BY_USER = 150;
        public static final int HOME_PAGE = 200;
        public static final int NEWS_LIST = 201;

        public static final int SHARE_PAGE = 300;
    }

    public static class PageData {

        public static class News {
            public static final int SUMMARY_BLOCK_ID = 15110;

            public static final int SUMMARY_IMAGE_URL = 15112;
            public static final int SUMMARY_TEXT = 15113;
            public static final int SUMMARY_HTML = 15114;

            public static final int SUMMARY_ROOT = 15111;
            public static final int SUMMARY_TYPE_WITH_IMAGE = 15201;
            public static final int SUMMARY_TYPE_WITH_TEXT = 15202;
            public static final int SUMMARY_TYPE_COMPLEX = 15203;
            public static final int SUMMARY_TYPE_WITH_HTML = 15204;

        }

        public static class CommonPageData {
            //date -> data of creation, int - userId,
            public static final int CREATION_INFO = 205;
        }

        public static class ContentView {
            public static final int TEXT_VIEW =  20010;
            public static final int IMAGE_VIEW =  20011;
            public static final int TITLE_VIEW =  20012;
        }

        public static class ContentData {
            public static final int STYLE =  20009;
            public static final int SIMPLE_TEXT =  30010;
            public static final int IMAGE_URL =  30011;
            public static final int TITLE =  30012;

        }
    }

    public static class UserDataTypes {
        public static final int SESSION_ID = 5000;

        public static final int REGISTRATION_INFO = 200;
        public static final int REGISTRATION_EMAIL = 201;
        public static final int REGISTRATION_LOGIN = 202;
        public static final int REGISTRATION_PASSWORD = 203;
        public static final int REGISTRATION_VK_ID = 204;

        public static final int ADDITIONAL_INFO = 100;
        public static final int ADDITIONAL_FIRST_NAME = 111;
        public static final int ADDITIONAL_LAST_NAME = 112;
        public static final int ADDITIONAL_WEB_SITE = 113;

        public static final int NEWS_PAGE = 500;
    }

    public static class Platform {
        public static final int DESKTOP = 0;

        public static final int PHONE = 1;
        public static final int TABLET = 2;

        public static final int ANDROID = 10;
        public static final int ANDROID_PHONE = 11;
        public static final int ANDROID_TABLET = 12;

        public static final int IOS = 30;
        public static final int IPHONE = 31;
        public static final int IPAD = 32;

        public static final int DEFAULT = DESKTOP;

        private static HashMap<String, Integer> supportedNames = new HashMap<>();

        static {
            supportedNames.put("phone".toLowerCase(), PHONE);
            supportedNames.put("tablet".toLowerCase(), PHONE);

            supportedNames.put("android".toLowerCase(), ANDROID);
            supportedNames.put("android-phone".toLowerCase(), ANDROID_PHONE);
            supportedNames.put("android-tablet".toLowerCase(), ANDROID_TABLET);

            supportedNames.put("ios", IOS);
            supportedNames.put("iphone", IPHONE);
            supportedNames.put("ipad", IPAD);

        }

        public static int fromString(String platformHeader) {
            int result;
            if(StringUtils.isEmpty(platformHeader)) {
                result = DEFAULT;
            } else  {
                Integer supported = supportedNames.get(platformHeader.toLowerCase());
                if (supported !=null ) {
                    result = supported.intValue();
                } else {
                    result = DEFAULT;
                }
            }
            return result;
        }

        public static boolean isPhone(int platform) {
            boolean result = platform != DESKTOP && (platform == PHONE || platform == ANDROID || platform == IOS || platform == ANDROID_PHONE || platform == IPHONE);
            return result;
        }
    }
}
