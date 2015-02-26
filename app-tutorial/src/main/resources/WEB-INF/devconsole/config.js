app.config(function($stateProvider, $urlRouterProvider) {

});

app.run(function($translatePartialLoader) {
	$translatePartialLoader.addPart('/apps/app-tutorial/devconsole/locales');
});

//# sourceURL=apps/app-tutorial/devconsole/config.js