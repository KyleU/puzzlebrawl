/* global define:false */
define([], function () {
  'use strict';

  function Navigation(game) {
    this.game = game;
    var self = this;

    window.addEventListener('hashchange', function() {
      self.navigate();
    }, false);

    var titleLabel = document.getElementById('title');
    if(titleLabel !== null && titleLabel !== undefined) {
      titleLabel.addEventListener('click', function(event) {
        window.location.hash = '#home';
      }, false);
    }
  }

  Navigation.prototype.navigate = function() {
    var h = window.location.hash;
    if(h.charAt(0) === '#') {
      h = h.substr(1);
    }
    switch(h) {
      case '':
      case 'home':
      case 'menu':
        this.game.panels.show('menu');
        break;
      case 'instructions':
        this.game.panels.show('instructions');
        break;
      case 'test':
        this.game.panels.show('test');
        break;
      case 'scenario':
        this.game.panels.show('scenario');
        break;
      case 'options':
        this.game.panels.show('options');
        break;
      default:
        if(this.game.playmat !== undefined) {
          this.game.playmat.resignIfPlaying();
        }
        this.game.panels.show('gameplay');
        this.game.send('StartBrawl', { 'scenario': h });
        break;
    }
  };

  return Navigation;
});
