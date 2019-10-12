# Table of Contents 
   * [µC-Graphics-Tools](#µc-graphics-tools)
      * [General usage](#general-usage)
      * [Currently available tools](#currently-available-tools)
         * [FontEncoder - <em>fenc</em>](#fontencoder---fenc)
         * [ImageEncoder - <em>inc</em>](#imageencoder---inc)
      * [Tips](#tips)
         * [Font creation](#font-creation)
         * [Examples](#examples)

# µC-Graphics-Tools

... is a collection of tools used in the context
of visualizations with microcontrollers. The counterpart
project/repository will be __µc-Graphics__. Each tool is
identified by a tool key (simple string). Each tool has
at least one supported target platform, required and
optional arguments.

## General usage

µC-Graphics-tools is a command line application that
requires Java 1.8 to run. Download the jar
__uc-graphics-tools.jar__ from the directory __jars/__.

To print the general usage information call:

``java -jar uc-graphics-tools.jar``

To execute a certain tool append the ``-T`` argument:

``java -jar uc-graphics-tools.jar -Tkey_of_tool ... [tool_arguments] ...``

Any argument value containing spaces must be enclosed in quotes.



## Currently available tools

### FontEncoder - *fenc*

This tool encodes bw bitmap fonts as byte arrays. The input must
be an image with ``width = char_width * 16`` and
``height = char_height * 16``. Supported are fonts with 256
characters - character codes ranging from 0 to 255. The font will
be encoded as a byte array and exported as a ready to use
header file. Their are two available formats:

- Simple byte chain (format key __'bc'__)
    - Byte 0: settings byte
        - Bit 7 == 1: format is 'bc' (here, this bit is set)
        - Bit 6 == 1: format is 'bcs'
        - Bit 5 == 1: pixel direction is 'hv' (pixels are sorted left to right, top to bottom)
        - Bit 4 == 1: pixel direction is 'vh' (pixels are sorted top to bottom, left to right)
    - Byte 1: width of characters
    - Byte 2: height of characters
    - Byte 3 & up: pixels (__encoded LSB__, every last char byte zero padded)
        - For every char:
            - One hasPixels-flag byte: if char not empty 1, if char empty 0
            - Then the pixel bytes follow (pixels as bits LSB) (``pixel_bytes_per_char = (width*height/8) + ((width/height) % 8) > 0 ? 1 : 0)``)
            - __Attention!: Even for empty characters / not drawn ones the pixel
            data is given (all zeros): This creates a huge overhead!__
    - The char index can be computed as follows:
        - ``pixel_bytes_per_char = (width*height/8) + ((width/height) % 8) > 0 ? 1 : 0)``
        - ``bytes_per_char = pixel_bytes_per_char + 1 // hasPixels-flag byte``
        - ``char_index = char_code * bytes_per_char + 3``
    - This format is suited for systems with a lot of memory. As
    the first byte index of a char can be directly calculated, it is
    not necessary to search the char start in the byte array - this
    saves a lot of computation time.
            

- Byte chain with search (format key __'bcs'__):
    - Byte 0: settings byte
        - Bit 7 == 1: format is 'bc' 
        - Bit 6 == 1: format is 'bcs' (here, this bit is set)
        - Bit 5 == 1: pixel direction is 'hv' (pixels are sorted left to right, top to bottom, LSB)
        - Bit 4 == 1: pixel direction is 'vh' (pixels are sorted top to bottom, left to right, LSB)
    - Byte 3 & up: pixels (__encoded LSB__,  every last char byte zero padded)
        - For every char:
            - One hasPixels-flag byte: if char not empty 1, if char empty 0
            - If the character is empty, the next byte will be the hasPixels-flag
            byte of the next character. If the character is not empty
            the pixel bytes follow (pixels as bits LSB) (``pixel_bytes_per_char = (width*height/8) + ((width/height) % 8) > 0 ? 1 : 0)``)
    - __Attention:__ in contrast to format __'bc'__ the char_index
    can not be calculated because it is unknown which characters are empty
    and do not contain pixel bytes. This way you have to iterate over
    the byte array and check every hasPixels-flag byte until the wanted
    character has been reached. __This format saves a lot of memory if not all
    chatacters are used, but increases the computation time a little__.
         

Call the FontEncoder by using the key __fenc__:

``java -jar uc-graphics-tools.jar -Tfenc ...``

Required arguments are:
- ``-I: input file``
- ``-P: target platform, currently only 'avrgcc' supported``
- ``-D: pixel direction, 'hv' (encode by left to right, top to bottom) or 'vh' (top to bottom, left to right)``
- ``-F: format, either 'bc' or 'bcs'``

Optional arguments are:
- ``-O: output file or directory``

Supported target platforms:
- ``AVR-GCC: key = avrgcc, font will be encoded as byte array using the progmem option``

### ImageEncoder - *inc*

This tool encodes images as byte arrays and exports them as
ready to use header files. __The maximum image size
is 255x255__ (because width and height are encoded in two bytes).
This is sufficient for most µC-Projects, because often LCD with maximum 128x64 pixels are used.

There is only one format the images will be encoded as:

- Byte 0: settings byte
    - Bit 5 == 1: pixel direction is 'hv' (pixels are sorted left to right, top to bottom, LSB)
    - Bit 4 == 1: pixel direction is 'vh' (pixels are sorted top to bottom, left to right, LSB)
- Byte 1: width of image
- Byte 2: height of image
- Byte 3 & up: pixels
    - Pixels are __encoded LSB__, each pixel is represented by a bit in the byte chain
    - ``bytes_per_img = (width * height / 8) + ( ((width * height) % 8) > 0 ? 1 : 0 )``
    - the last byte will be zero padded
 
Call the FontEncoder by using the key __inc__:

``java -jar uc-graphics-tools.jar -Tinc ...``

Required arguments are:
- ``-I: input file``
- ``-P: target platform, currently only 'avrgcc' supported``
- ``-D: pixel direction, 'hv' (encode by left to right, top to bottom) or 'vh' (top to bottom, left to right)``

Optional arguments are:
- ``-O: output file or directory``

Supported target platforms:
- ``AVR-GCC: key = avrgcc, font will be encoded as byte array using the progmem option``

## Tips

### Font creation

To create fonts you can use any image editor. A good tool is GIMP.
It is a free tool. Create an image with ``width = char_width * 16`` and
``height = char_height * 16``. The go to __Image__ > __Configure Grid__
set the grid size to the character dimensions. Then go to __View__ >
__Show Grid__ et voila - now you can easily design your font in the grid.

### Examples

Check out the directories __fonts/__ and __imgs/__ for example images and
exported header files.