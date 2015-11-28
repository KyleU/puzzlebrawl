/* global define:false */
/* global Phaser:false */
define(['gem/GemSprites'], function (GemSprites) {
  'use strict';

  function Gem(model, game) {
    this.model = model;

    //Phaser.Sprite.call(this, game, 0, 0);
    //this.setTexture(this.game.gemTextures.getTexture(this.model));

    var idx = GemSprites.spriteFor(this.model);
    Phaser.Sprite.call(this, game, 0, 0, 'gems', idx);

    this.name = 'gem-' + this.model.id;
    this.anchor.setTo(0.0, 1.0);
  }

  Gem.prototype = Phaser.Sprite.prototype;
  Gem.prototype.constructor = Gem;

  Gem.prototype.updateModel = function(newModel) {
    this.model = newModel;
    var idx = GemSprites.spriteFor(this.model);
    return idx;
  };

  return Gem;
});
