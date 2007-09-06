varying vec4 Vertex;
void main() {
  vec4 output;
  vec4 output2;
  vec4 FragColor;
  output2 = sin(Vertex);
  output = atan(output2);
  FragColor = output;
  gl_FragColor = FragColor;
}