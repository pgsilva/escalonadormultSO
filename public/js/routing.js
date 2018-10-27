app.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');

    $stateProvider
        //Routing home view
        .state('form', {
            url: '/',
            views: {
                "body": {
                    templateUrl: '../../views/form.html',
                    controller: 'ProcessoController'
                },
                "navbar": { templateUrl: '../../views/navbar.html' }
            }
        })

});