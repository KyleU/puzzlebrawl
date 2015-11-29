/* global define:false */
/* global Phaser:false */
define(['gem/GemSprites'], function(GemSprites) {
  'use strict';

  function keyFor(gem) {
    var key = gem.color;
    if(gem.width === undefined) { key += '1'; } else { key += gem.width; }
    if(gem.height === undefined) { key += '1'; } else { key += gem.height; }
    if(gem.crash) { key += 'c'; }
    if(gem.timer !== undefined) { key += gem.timer; }
    return key;
  }

  function GemTextures(game) {
    this.game = game;
    this.gemImages = [];
    for(var texIdx = 0; texIdx < 80; texIdx++) {
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
      if(gem.width === undefined) { width = 1; } else { width = gem.width; }
      if(gem.height === undefined) { height = 1; } else { height = gem.height; }
      var bd = this.game.add.bitmapData(width * 128, height * 128);
      for(var x = 0; x < width; x++) {
        for(var y = 0; y < height; y++) {
          var texIdx = GemSprites.spriteFor(gem, x, y);
          bd.copy(this.gemImages[texIdx], 0, 0, 128, 128, x * 128, (height - y - 1) * 128);
        }
      }

      this.gemTextures[key] = bd.texture;
    } else {
      throw 'Texture for [' + key + '] already loaded.';
    }
  };

  return GemTextures;
});
