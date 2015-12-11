/* global requirejs:false */
requirejs.config({
  baseUrl: '/assets/javascripts',
  paths: {
    lib: '/assets/lib'
  }
});

requirejs(['game/Game'], function(Game) {
  'use strict';
  window.game = new Game();
});
