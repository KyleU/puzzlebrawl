/* global requirejs:false */
requirejs.config({
  baseUrl: '/assets/javascripts',
  paths: {
    lib: '/assets/lib'
  }
});

requirejs(['Sunscreen'], function(Sunscreen) {
  'use strict';
  window.sunscreen = new Sunscreen();
});
