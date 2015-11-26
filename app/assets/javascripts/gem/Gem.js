/* global define:false */
/* global Phaser:false */
define(['gem/GemSprites'], function (GemSprites) {
  'use strict';

  function Gem(model, game) {
    this.model = model;
    var idx = GemSprites.spriteFor(this.model);
    Phaser.Sprite.call(this, game, 0, 0, 'gems', idx);

    this.name = 'gem-' + this.model.id;
    this.anchor.setTo(0.5, 0.5);
  }

  Gem.prototype = Phaser.Sprite.prototype;
  Gem.prototype.constructor = Gem;

  return Gem;
});
