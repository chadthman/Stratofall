package com.me.stratofall.utils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
 
public class ShaderUtils {
    private static final String SIMPLE_PASSTHROUGH_VERT = "attribute vec4 a_position;\r\n"
            + "attribute vec2 a_texCoord0;\r\n" + "attribute vec4 a_color;\r\n" + "uniform mat4 u_projTrans;\r\n"
            + "varying vec4 v_color;\r\n" + "varying vec2 v_texCoords;\r\n" + "void main() {\r\n"
            + "    v_color = a_color;\r\n" + "    v_texCoords = a_texCoord0;\r\n"
            + "    gl_Position = u_projTrans * a_position;\r\n" + "}";
    private static final String WOBBLY_FRAGMENT = "#ifdef GL_ES\r\n" + "    precision mediump float;\r\n"
            + "#endif\r\n" + "uniform sampler2D u_texture;\r\n" + "varying vec4 v_color;\r\n"
            + "varying vec2 v_texCoords;\r\n" + "\r\n" + "uniform float time;\r\n" + "uniform float wobble;\r\n"
            + "vec3 mod289(vec3 x) {\r\n" + "  return x - floor(x * (1.0 / 289.0)) * 289.0;\r\n" + "}\r\n"
            + "vec2 mod289(vec2 x) {\r\n" + "  return x - floor(x * (1.0 / 289.0)) * 289.0;\r\n" + "}\r\n"
            + "vec3 permute(vec3 x) {\r\n" + "  return mod289(((x*34.0)+1.0)*x);\r\n" + "}\r\n"
            + "float snoise(vec2 v) {\r\n" + "  const vec4 C = vec4(0.211324865405187, \r\n"
            + "                      0.366025403784439,  \r\n" + "                     -0.577350269189626,  \r\n"
            + "                      0.024390243902439);\r\n" + "  vec2 i  = floor(v + dot(v, C.yy) );\r\n"
            + "  vec2 x0 = v -   i + dot(i, C.xx);\r\n" + "\r\n" + "  vec2 i1;\r\n"
            + "  i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);\r\n" + "  vec4 x12 = x0.xyxy + C.xxzz;\r\n"
            + "  x12.xy -= i1;\r\n" + "  i = mod289(i);\r\n"
            + "  vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))\r\n"
            + "                + i.x + vec3(0.0, i1.x, 1.0 ));\r\n" + "\r\n"
            + "  vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);\r\n"
            + "  m = m*m ;\r\n" + "  m = m*m ;\r\n" + "  vec3 x = 2.0 * fract(p * C.www) - 1.0;\r\n"
            + "  vec3 h = abs(x) - 0.5;\r\n" + "  vec3 ox = floor(x + 0.5);\r\n" + "  vec3 a0 = x - ox;\r\n"
            + "  m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );\r\n" + "  vec3 g;\r\n"
            + "  g.x  = a0.x  * x0.x  + h.x  * x0.y;\r\n" + "  g.yz = a0.yz * x12.xz + h.yz * x12.yw;\r\n"
            + "  return 130.0 * dot(m, g);\r\n" + "}\r\n" + "\r\n" + "void main() {\r\n"
            + "    vec2 distort = 0.005 * vec2(snoise(v_texCoords + vec2(0.0, time * wobble)),\r\n"
            + "                              snoise(v_texCoords + vec2(time * wobble, 0.0)));\r\n"
            + "    vec4 texColor0 = texture2D(u_texture, v_texCoords + distort);\r\n"
            + "    gl_FragColor = v_color * texColor0;\r\n" + "}";
    private static ShaderProgram wobbly;
 
    public static void init() {
        wobbly = new ShaderProgram(SIMPLE_PASSTHROUGH_VERT, WOBBLY_FRAGMENT);
    }
 
    public static void startWobbly(SpriteBatch batch, float time, float wobble) {
        batch.setShader(wobbly);
        wobbly.setUniformf("time", time);
        wobbly.setUniformf("wobble", wobble);
    }
 
    public static void end(SpriteBatch batch) {
        batch.setShader(null);
    }
}