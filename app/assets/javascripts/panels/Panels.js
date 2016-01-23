/* global define:false */
define([], function () {
  'use strict';

  function Panels() {
    this.splash = document.getElementById('splash-panel');
    this.connecting = document.getElementById('connecting-panel');
    this.menu = document.getElementById('menu-panel');
    this.tests = document.getElementById('test-panel');
    this.scenarios = document.getElementById('scenario-panel');
    this.instructions = document.getElementById('instructions-panel');
    this.options = document.getElementById('options-panel');
    this.gameplay = document.getElementById('game-container');

    this.active = this.splash;
  }

  Panels.prototype.show = function(key) {
    this.active.classList.remove('on');
    this.active.classList.add('off');

    switch(key) {
      case 'connecting':
        this.active = this.connecting;
        break;
      case 'menu':
        this.active = this.menu;
        break;
      case 'instructions':
        this.active = this.instructions;
        break;
      case 'scenario':
        this.active = this.scenarios;
        break;
      case 'test':
        this.active = this.tests;
        break;
      case 'options':
        this.active = this.options;
        break;
      case 'gameplay':
        this.active = this.gameplay;
        break;
      default:
        throw 'Invalid key [' + key + '].';
    }

    this.active.classList.remove('off');
    this.active.classList.add('on');
  };

  return Panels;
});
