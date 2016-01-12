/* global define:false */
define([], function() {
  var Audio = function(game) {
    this.game = game;
    this.sound = this.game.add.audio('audio');
    this.sound.allowMultiple = true;

    this.sound.addMarker('death', 1, 1.0);
    this.sound.addMarker('hit', 3, 0.5);
    this.sound.addMarker('escape', 4, 3.2);
    this.sound.addMarker('meow', 8, 0.5);
    this.sound.addMarker('numkey', 9, 0.1);
    this.sound.addMarker('ping', 10, 1.0);
    this.sound.addMarker('death', 12, 4.2);
    this.sound.addMarker('shot', 17, 1.0);
    this.sound.addMarker('squit', 19, 0.3);
  };

  Audio.prototype.play = function(key) {
    this.sound.play(key);
  };

  return Audio;
});
