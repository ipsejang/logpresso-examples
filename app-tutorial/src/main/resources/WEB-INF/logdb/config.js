app.config(function($stateProvider, $urlRouterProvider) {

});

app.run(function($translatePartialLoader) {
	$translatePartialLoader.addPart('/apps/app-tutorial/logdb/locales');
});

//# sourceURL=apps/app-tutorial/logdb/config.js