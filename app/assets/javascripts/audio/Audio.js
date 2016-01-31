/* global define:false */
define([], function() {
  var Audio = function(game) {
    this.enabled = false;
    if(this.enabled) {
      this.game = game;
      this.sound = this.game.add.audio('audio');
      this.sound.allowMultiple = true;

      this.sound.addMarker('drop', 1, 1.0);
      this.sound.addMarker('crash', 9, 0.1);
      this.sound.addMarker('kaching', 10, 1.0);
      this.sound.addMarker('pow', 17, 1.0);
      this.sound.addMarker('move', 19, 0.3);

      this.sound.addMarker('hit', 3, 0.5);
      this.sound.addMarker('escape', 4, 3.2);
      this.sound.addMarker('meow', 8, 0.5);
      this.sound.addMarker('death', 12, 4.2);
    }
  };

  Audio.prototype.play = function(key) {
    if(this.enabled) {
      this.sound.play(key);
    }
  };

  return Audio;
});
