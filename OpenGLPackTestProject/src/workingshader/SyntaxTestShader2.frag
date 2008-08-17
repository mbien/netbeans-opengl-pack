uniform float pixCoordYOffset;
vec2 pixcoord = vec2(gl_FragCoord.x, pixCoordYOffset-gl_FragCoord.y);
uniform sampler2D baseImg;
const int MAX_KERNEL_SIZE = 127;
uniform vec4 kvals[127];
uniform int kernelSize;
uniform vec4 shadowColor;

void main() {


    int i;
    float sum = 0.0;
    {
        sum += kvals[0].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[0].xy).a;
    }
    {
        sum += kvals[1].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[1].xy).a;
    }
    {
        sum += kvals[2].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[2].xy).a;
    }
    {
        sum += kvals[3].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[3].xy).a;
    }
    {
        sum += kvals[4].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[4].xy).a;
    }
    {
        sum += kvals[5].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[5].xy).a;
    }
    {
        sum += kvals[6].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[6].xy).a;
    }
    {
        sum += kvals[7].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[7].xy).a;
    }
    {
        sum += kvals[8].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[8].xy).a;
    }
    {
        sum += kvals[9].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[9].xy).a;
    }
    {
        sum += kvals[10].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[10].xy).a;
    }
    {
        sum += kvals[11].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[11].xy).a;
    }
    {
        sum += kvals[12].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[12].xy).a;
    }
    {
        sum += kvals[13].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[13].xy).a;
    }
    {
        sum += kvals[14].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[14].xy).a;
    }
    {
        sum += kvals[15].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[15].xy).a;
    }
    {
        sum += kvals[16].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[16].xy).a;
    }
    {
        sum += kvals[17].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[17].xy).a;
    }
    {
        sum += kvals[18].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[18].xy).a;
    }
    {
        sum += kvals[19].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[19].xy).a;
    }
    if (20 < kernelSize)
    {
    sum += kvals[20].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[20].xy).a;
    }
    if (21 < kernelSize)
    {
    sum += kvals[21].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[21].xy).a;
    }
    if (22 < kernelSize)
    {
    sum += kvals[22].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[22].xy).a;
    }
    if (23 < kernelSize)
    {
    sum += kvals[23].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[23].xy).a;
    }
    if (24 < kernelSize)
    {
    sum += kvals[24].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[24].xy).a;
    }
    if (25 < kernelSize)
    {
    sum += kvals[25].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[25].xy).a;
    }
    if (26 < kernelSize)
    {
    sum += kvals[26].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[26].xy).a;
    }
    if (27 < kernelSize)
    {
    sum += kvals[27].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[27].xy).a;
    }
    if (28 < kernelSize)
    {
    sum += kvals[28].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[28].xy).a;
    }
    if (29 < kernelSize)
    {
    sum += kvals[29].z * texture2D(baseImg, gl_TexCoord[0].st + kvals[29].xy).a;
    }
    gl_FragColor = sum * shadowColor;

    {{{{/*codeblock test*/}}}}


}