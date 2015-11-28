/* global define:false */
/* global Phaser:false */
define(['gem/GemSprites'], function(GemSprites) {
  'use strict';

  function keyFor(gem) {
    var key = gem.color;
    if(gem.w === undefined) { key += '1'; } else { key += gem.w; }
    if(gem.h === undefined) { key += '1'; } else { key += gem.h; }
    return key;
  }

  function GemTextures(game) {
    this.game = game;
    this.gemImages = [];
    for(var texIdx = 0; texIdx < 64; texIdx++) {
      this.gemImages.push(new Phaser.Image(game, 0, 0, 'gems', texIdx));
    }
    this.gemTextures = {};

    this.getTexture({color: 'r'});
    this.getTexture({color: 'g'});
    this.getTexture({color: 'b'});
    this.getTexture({color: 'y'});
    this.getTexture({color: 'r', crash: true});
    this.getTexture({color: 'g', crash: true});
    this.getTexture({color: 'b', crash: true});
    this.getTexture({color: 'y', crash: true});
  }

  GemTextures.prototype.getTexture = function(gem) {
    var key = keyFor(gem);
    if(this.gemTextures[key] === undefined) {
      this.loadTexture(gem, key);
    }
    return this.gemTextures[key];
  };

  GemTextures.prototype.loadTexture = function(gem, key) {
    if(this.gemTextures[key] === undefined) {
      var width, height;
      if(gem.w === undefined) { width = 1; } else { width = gem.w; }
      if(gem.h === undefined) { height = 1; } else { height = gem.h; }

      //var tex = this.game.add.bitmapData(width * 128, height * 128);

      var texIdx = GemSprites.spriteFor(gem);
      //tex.draw(this.gemImages[texIdx], 0, 0);

      this.gemTextures[key] = this.gemImages[texIdx];
    } else {
      throw 'Texture for [' + key + '] already loaded.';
    }
  };

  return GemTextures;
});
