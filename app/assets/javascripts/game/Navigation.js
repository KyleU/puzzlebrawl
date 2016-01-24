/* global define:false */
define(['utils/Status'], function (Status) {
  'use strict';

  function Navigation(game) {
    this.game = game;
    var self = this;

    window.addEventListener('hashchange', function() {
      self.navigate();
    }, false);

    var titleLabel = document.getElementById('title-text');
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
        Status.setStatus('Home');
        this.game.panels.show('menu');
        break;
      case 'instructions':
        Status.setStatus('Instructions');
        this.game.panels.show('instructions');
        break;
      case 'test':
        Status.setStatus('Tests');
        this.game.panels.show('test');
        break;
      case 'scenario':
        Status.setStatus('Scenarios');
        this.game.panels.show('scenario');
        break;
      case 'options':
        Status.setStatus('Options');
        this.game.panels.show('options');
        break;
      default:
        if(this.game.playmat !== undefined) {
          this.game.playmat.resignIfPlaying();
        }
        Status.setStatus('Starting Brawl');
        this.game.panels.show('gameplay');
        this.game.send('StartBrawl', { 'scenario': h });
        break;
    }
  };

  return Navigation;
});
