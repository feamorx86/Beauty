var menu = angular.module('appMenuEditor', []);

menu.config( function ($locationProvider) {
    $locationProvider.html5Mode(true);
});

menu.controller('menuEditor', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var baseUrl = "/mobile/menu_editor?format=json&action=";
    $scope.componentId = $location.search().component_id;

    $scope.menuItemTypes = null;
    $scope.selectedMenuItemType = null;

    $scope.menuItemStyles = null;
    $scope.selectedMenuStyle = null;

    $scope.showMenuItemsEditor = true;
    $scope.menuItems = null;
    $scope.selectedMenuItem = null;

    $scope.getMenuItemTypes = function() {
        $http.get(baseUrl+"list_menu_item_types")
                    .then(function (responce) {
                        if (responce.data.status == "success") {
                            $scope.menuItemTypes = responce.data.types;
                        } else {
                            alert('Fail to list menu item types, status: '+responce.data.status)
                        }
                    });
    };

    $scope.editMenuType = function(typeId, typeType, typeName) {
        if (typeId && typeType && typeName) {
            $http.get(baseUrl + "edit_menu_item_type&id=" + typeId + "&type=" + typeType + "&name=" + typeName)
                .then(function (responce) {
                    if (responce.data.status == "success") {
                        alert('Saved');
                    } else {
                        alert('Fail to list menu item types, status: ' + responce.data.status)
                    }
                    $scope.getMenuItemTypes();
                });
        }
    };

    $scope.findItemType = function(typeId) {
        if (typeId) {
            var result = [];
            angular.forEach($scope.menuItemTypes, function (menuItemType) {
                if (menuItemType.id == typeId) {
                    this.push(menuItemType);
                }
            }, result);
            if (result && result.length > 0) {
                return result[0];
            } else {
                return null;
            }
        }
        return null;
    };

    $scope.findItemTypeByType = function(typeValue) {
        if (typeValue) {
            var result = [];
            angular.forEach($scope.menuItemTypes, function (menuItemType) {
                if (menuItemType.type == typeValue) {
                    this.push(menuItemType);
                }
            }, result);
            if (result && result.length > 0) {
                return result[0];
            } else {
                return null;
            }
        }
        return null;
    };

    $scope.getMenuItemStyles = function() {
        $http.get(baseUrl+"list_item_styles")
            .then(function (responce) {
                if (responce.data.status == "success") {
                    $scope.menuItemStyles = responce.data.styles;
                } else {
                    alert('Fail to list menu item styles, status: '+responce.data.status)
                }
            });
    };

    $scope.editMenuItemStyle = function(styleId, styleType, styleParams) {
        if (styleId && styleType && styleParams) {
            $http.get(baseUrl + "edit_style&id=" + styleId + "&type=" + styleType + "&params=" + styleParams)
                .then(function (responce) {
                    if (responce.data.status == "success") {
                        alert('Saved');
                        $scope.getMenuItemStyles();
                    } else {
                        alert('Fail to list menu item types, status: ' + responce.data.status)
                    }
                    $scope.getMenuItemTypes();
                });
        }
    };

    $scope.removeMenuItemStyle = function(styleId) {
        if (styleId) {
            $http.get(baseUrl+"remove_style&id="+styleId)
                .then(function (responce) {
                    if (responce.data.status == 'success') {
                        $scope.getMenuItemStyles();
                    } else {
                        alert('Fail to remove style with id: '+styleId+', error : '+responce.data.error_message);
                    }
                })
        }
    };

    $scope.addMenuItemStyle = function(styleType, styleParams) {
        if (styleType && styleParams) {
            var itemType = $scope.findItemTypeByType(styleType);
            if (itemType) {
                $http.get(baseUrl+'add_style&type='+styleType+'&params='+styleParams)
                    .then(function (responce) {
                        if (responce.data.status == 'success') {
                            $scope.getMenuItemStyles();
                        } else {
                            alert('Fail to add style with, error : '+responce.data.error_message);
                        }
                    });
            }
        }
    };

    $scope.getMenuItems = function() {
        if ($scope.componentId) {
            $http.get(baseUrl + "list_menu_items&component_id="+$scope.componentId)
                .then(function (responce) {
                    if (responce.data.status == "success") {
                        $scope.menuItems = responce.data.items;
                    } else {
                        alert('Fail to list menu items, status: ' + responce.data.status)
                    }
                });
        }
    };

    $scope.addMenuItem = function(itemType, itemConfig) {
        if (itemType && itemConfig && $scope.componentId) {
            var addedItemType = $scope.findItemTypeByType(itemType);
            if (addedItemType) {
                $http.get(baseUrl+'add_menu_item&type='+itemType+'&value='+itemConfig+'&component_id='+$scope.componentId)
                    .then(function (responce) {
                        if (responce.data.status == 'success') {
                            $scope.getMenuItems();
                        } else {
                            alert('Fail to add menu item, error : '+responce.data.error_message);
                        }
                    });
            }
        }
    };

    $scope.editMenuItem = function(itemId, itemType, itemConfig) {
        if (itemId && itemType && itemConfig && $scope.componentId) {
            var addedItemType = $scope.findItemTypeByType(itemType);
            if (addedItemType) {
                $http.get(baseUrl+'edit_menu_item&id='+itemId+'&type='+itemType+'&value='+itemConfig+'&component_id='+$scope.componentId)
                    .then(function (responce) {
                        if (responce.data.status == 'success') {
                            $scope.getMenuItems();
                        } else {
                            alert('Fail to update menu item, error : '+responce.data.error_message);
                        }
                    });
            }
        }
    };

    $scope.removeMenuItem = function(itemId) {
        if (itemId) {
            $http.get(baseUrl+"remove_menu_item&id="+itemId+'&component_id='+$scope.componentId)
                .then(function (responce) {
                    if (responce.data.status == 'success') {
                        $scope.getMenuItems();
                    } else {
                        alert('Fail to remove menu item with id: '+itemId+', error : '+responce.data.error_message);
                    }
                })
        }
    };

    $scope.getMenuItemTypes();
    $scope.getMenuItemStyles();
    $scope.getMenuItems();
}]);

