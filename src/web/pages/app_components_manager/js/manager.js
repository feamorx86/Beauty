var app = angular.module('appDataManager', []);

app.controller('appsController', function ($scope, $http) {
    $scope.selectedApp = null;
    $scope.isComponentsVisible = false;
    $scope.selectApp = function (appId) {
        if (appId) {
            $http.get("/mobile/appdata?format=json&action=list_app_components&app_id=" + appId)
                .then(function (responce) {
                    $scope.selectedApp = responce.data;
                    $scope.isComponentsVisible = true;
                });
        } else {
            $scope.selectedApp = null;
            $scope.isComponentsVisible = false;
        }
    };
    $http.get('/mobile/appdata?format=json&action=list_all_components')
        .then(function(responce){
           $scope.all_components = responce.data;
        });
    $http.get('/mobile/appdata?format=json&action=list_apps')
        .then(function (responce) {
            $scope.apps_data = responce.data;
        });
    $scope.getComponent = function(id) {
        if (id) {
            var result = [];
            angular.forEach($scope.all_components.components, function (component) {
                if (component.id == id) {
                    this.push(component);
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
    $scope.removeComponent = function(appId, componentId) {
        if (appId && componentId) {
            $http.get('/mobile/appdata?format=json&action=remove_app_component&app_id='+appId+'&component_id='+componentId)
                .then(function (responce) {
                    if (responce.data.status == 'success') {
                        $scope.selectApp(appId);
                    }
                })
        }
    };

    $scope.addComponent = function(selectedComponentId, appId, componentComment){
        if (appId && selectedComponentId) {
            component = $scope.getComponent(selectedComponentId);
            if (component) {
                $http.get('/mobile/appdata?format=json&action=add_app_component&app_id=' + appId + '&component_type=' + component.type + '&component_comment=' + componentComment + '&component_link=' + component.id)
                    .then(function (responce) {
                        if (responce.data.status == 'success') {
                            $scope.selectApp(appId);
                        }
                    });
            }
        }
    };

});

