app.config(function($stateProvider, $urlRouterProvider) {

});

app.run(function($translatePartialLoader) {
	$translatePartialLoader.addPart('/apps/app-tutorial/crud/locales');
});

//# sourceURL=apps/app-tutorial/crud/config.js