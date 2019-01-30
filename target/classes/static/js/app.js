var app = angular.module('app',[]);

app.controller('StarWarsCRUDCtrl', ['$scope','StaWarsCRUDService', function ($scope, StaWarsCRUDService,$window) {

    $scope.updateCharacter = function () {
        StaWarsCRUDService.updateCharacter($scope.character.id,$scope.character.name,$scope.character.email)
            .then(function success(response){
                    $scope.message = 'Character data updated!';
                    $scope.errorMessage = '';
                },
                function error(response){
                    $scope.errorMessage = 'Error updating character!';
                    $scope.message = '';
                });
    }
    $scope.goToDetails = function(character) {
        $scope.character=character;
        console.log($scope.character);
        StaWarsCRUDService.goToDetails($scope.character.url)

    };

    $scope.getCharacter = function () {
        var params=window.location.search;
       // console.log('params '+params);
        var url=(params.substring( 5,params.length));
       // console.log('url '+url);
        if (url != null ) {
            StaWarsCRUDService.getCharacter(url)
                .then (function success(response){
                        $scope.character = response.data;
                        $scope.message = '';
                        $scope.errorMessage = '';
                        $scope.$title=$scope.character.name;

                        //check if in favourites
                        StaWarsCRUDService.checkCharacter(url)
                            .then (function success(response){
                                    $scope.character.favourite = response.data;
                                    console.log(" $scope.character.favourite >>"+ $scope.character.favourite)


                                },
                                function error(response){
                                    $scope.character.favourite='0';
                                    $scope.errorMessage = 'Error finding character!';
                                    $scope.message = '';
                                });

                    },
                    function error(response){
                        $scope.errorMessage = 'Error finding character!';
                        $scope.message = '';
                        $scope.character.favourite='0';
                    });
        }
        else {
            $scope.errorMessage = 'Please select one!';
            $scope.message = '';
        }


    }

    $scope.addFavourite = function () {
        var tempchar=angular.element(document.getElementById("mycharacter"))
        console.log(tempchar);
        if ($scope.character != null && $scope.character.url) {
            StaWarsCRUDService.UpdateFavourite('add',$scope.character.url)
                .then (function success(response){
                        $scope.message = 'Character added to favourites!';
                        $scope.errorMessage = '';
                       // window.location.reload();
                        $scope.character.favourite='1';
                    },
                    function error(response){
                    //console.log(response);
                        $scope.errorMessage = 'Error adding character! ' +response.data.errorMessage;
                        $scope.message = '';
                    });
        }
        else {
            $scope.errorMessage = 'Please select one!';
            $scope.message = '';
        }
    }

    $scope.removeFavourite = function () {
       // console.log("character: "+$scope.character);
        StaWarsCRUDService.UpdateFavourite('remove',$scope.character.url)
            .then (function success(response){
                    $scope.message = 'Character removed from favourite!';
                    $scope.errorMessage='';
                    //window.location.reload();
                    $scope.character.favourite='0';
                },
                function error(response){
                    $scope.errorMessage =  +response.data.errorMessage;
                    $scope.message='';
                })
    }

    $scope.getAllCharacters = function () {
        StaWarsCRUDService.getAllCharacters()
            .then(function success(response){
                    $scope.characters = response.data;
                    $scope.message='';
                    $scope.errorMessage = '';
                    // console.log($scope.characters);
                },
                function error (response ){
                    $scope.message='';
                    $scope.errorMessage = 'Error getting characters! ';
                });
    }
    $scope.getNextCharacters = function () {
        StaWarsCRUDService.getNextCharacters()
            .then(function success(response){
                    $scope.characters = response.data;
                    $scope.message='';
                    $scope.errorMessage = '';
                    // console.log($scope.characters);
                },
                function error (response ){
                    $scope.message='';
                    $scope.errorMessage = 'Error getting characters! ';
                });
    }
    $scope.getPreviousCharacters = function () {
        StaWarsCRUDService.getPreviousCharacters()
            .then(function success(response){
                    $scope.characters = response.data;
                    $scope.message='';
                    $scope.errorMessage = '';
                    // console.log($scope.characters);
                },
                function error (response ){
                    $scope.message='';
                    $scope.errorMessage = 'Error getting characters! ';
                });
    }

}]);

app.service('StaWarsCRUDService',['$http', function ($http) {

    this.getCharacter = function getCharacter(url){
        return $http({
            method: 'GET',
            url: url
        });
    }

    this.goToDetails = function goToDetails(url){

        // window.location='/details?url=' + url;
        window.open('/details?url=' + url, '_blank');
    }


    this.UpdateFavourite = function UpdateFavourite(action, url){
        return $http({
            method: 'POST',
            url: 'updatefavorite',
            data: {action:action, url:url}
        });
    }

    this.deleteCharacter = function deleteCharacter(id){
        return $http({
            method: 'DELETE',
            url: 'characters/'+id
        })
    }

    this.updateCharacter = function updateCharacter(id,name,email){
        return $http({
            method: 'PATCH',
            url: 'characters/'+id,
            data: {name:name, email:email}
        })
    }

    this.getAllCharacters = function getAllCharacters(){
        return $http({
            method: 'GET',
            url: 'characters'
        });
    }

    this.getNextCharacters = function getNextCharacters(){
        return $http({
            method: 'GET',
            url: 'getnext'
        });
    }

    this.getPreviousCharacters = function getPreviousCharacters(){
        return $http({
            method: 'GET',
            url: 'getprevious'
        });
    }

    this.checkCharacter = function checkCharacter(url){
        return $http({
            method: 'GET',
            url: 'checkfavorite?url='+url
        });
    }



}]);