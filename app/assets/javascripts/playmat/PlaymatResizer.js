/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var PlaymatResizer = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatResizer.prototype.refreshLayout = function() {
    var p = this.playmat;
    var originalSize = [p.w, p.h];

    p.w = 832;
    p.h = 1600;

    if(p.w !== originalSize[0] || p.h !== originalSize[1]) {
      this.resize();
    }
  };

  PlaymatResizer.prototype.resize = function() {
    var p = this.playmat;
    var totalHeight = p.game.world.height;

    var widthRatio = p.game.world.width / p.w;
    var heightRatio = totalHeight / p.h;

    var newPosition = p.position;
    var newScale = p.scale;

    if(widthRatio < heightRatio) {
      newScale = new Phaser.Point(widthRatio, widthRatio);
      var yOffset = (totalHeight - (p.h * widthRatio)) / 2;
      if(yOffset > 0 || p.y !== 0) {
        newPosition = new Phaser.Point(0, yOffset);
      }
    } else {
      newScale = new Phaser.Point(heightRatio, heightRatio);
      var xOffset = (p.game.world.width - (p.w * heightRatio)) / 2;
      if(xOffset > 0 || p.x !== 0) {
        newPosition = new Phaser.Point(xOffset, 0);
      }
    }

    if(p.game.initialized) {
      p.game.add.tween(p.scale).to({x: newScale.x, y: newScale.y}, 500, Phaser.Easing.Quadratic.InOut, true);
      p.game.add.tween(p.position).to({x: newPosition.x, y: newPosition.y}, 500, Phaser.Easing.Quadratic.InOut, true);
    } else {
      p.scale = newScale;
      p.position = newPosition;
    }
  };

  return PlaymatResizer;
});
