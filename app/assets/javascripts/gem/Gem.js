/* global define:false */
/* global Phaser:false */
define(['gem/GemSprites'], function (GemSprites) {
  'use strict';

  function Gem(model, game) {
    this.model = model;

    Phaser.Sprite.call(this, game);
    this.name = 'gem-' + this.model.id;
    this.setTexture(this.game.gemTextures.getTexture(this.model));
    this.anchor.setTo(0.0, 1.0);
  }

  Gem.prototype = Phaser.Sprite.prototype;
  Gem.prototype.constructor = Gem;

  Gem.prototype.updateModel = function(newModel) {
    this.model = newModel;
    this.setTexture(this.game.gemTextures.getTexture(this.model));
  };

  return Gem;
});
