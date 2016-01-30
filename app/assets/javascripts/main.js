/* global requirejs:false */
requirejs.config({
  baseUrl: '/assets/javascripts',
  paths: {
    lib: '/assets/lib'
  }
});

requirejs(['game/Game', 'utils/ErrorHandling'], function(Game, ErrorHandling) {
  'use strict';
  ErrorHandling.init();
  window.brawl = new Game();
});
