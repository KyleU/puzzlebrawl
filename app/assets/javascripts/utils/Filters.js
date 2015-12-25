/* global define:false */
/* global PIXI:false */
define([], function() {
  'use strict';

  var invert = new PIXI.AbstractFilter(
    [
      'precision mediump float;',
      '',
      'varying vec2 vTextureCoord;',
      '',
      'uniform float invert;',
      'uniform sampler2D uSampler;',
      '',
      'void main(void)',
      '{',
      '  gl_FragColor = texture2D(uSampler, vTextureCoord);',
      '  gl_FragColor.rgb = mix( (vec3(1)-gl_FragColor.rgb) * gl_FragColor.a, gl_FragColor.rgb, 1.0 - invert);',
      '}'
    ],
    {
      'invert': {type: '1f', value: 1}
    }
  );

  var twist = new PIXI.AbstractFilter(
    [
      'precision mediump float;',
      '',
      'varying vec2 vTextureCoord;',
      '',
      'uniform sampler2D uSampler;',
      'uniform float radius;',
      'uniform float angle;',
      'uniform vec2 offset;',
      '',
      'void main(void)',
      '{',
      '  vec2 coord = vTextureCoord - offset;',
      '  float dist = length(coord);',
      '',
      '  if (dist < radius)',
      '  {',
      '    float ratio = (radius - dist) / radius;',
      '    float angleMod = ratio * ratio * angle;',
      '    float s = sin(angleMod);',
      '    float c = cos(angleMod);',
      '    coord = vec2(coord.x * c - coord.y * s, coord.x * s + coord.y * c);',
      '  }',
      '',
      '  gl_FragColor = texture2D(uSampler, coord+offset);',
      '}'
    ],
    {
      radius: {type: '1f', value: 0.5},
      angle: {type: '1f', value: 5},
      offset: {type: '2f', value: {x: 0.5, y: 0.5}}
    }
  );

  return {
    invert: invert,
    twist: twist
  };
});
