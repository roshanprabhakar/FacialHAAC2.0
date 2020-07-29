# Facial HAAC 2.0
An image compressor for the general image compression of facial images. The core compression algorithm exploits the relative unimportance of color identity regions within an image. The compression occurs through a generation of k color space clusters where each cluster depicts a color in the compressed representation. 

Module Mapping
---
Documented Output: 
Collection of outputs, both using pixelation background compression and color clustering (JPEG) compression

FileAnalyzer: 
A program to rate the effectiveness of the general algorithm by human standards

MergedHAACCompressor:
A sub repo containing both the HAAC 1 compression program, as well as the newer clustering general compression algorithm

haar_knn:
The implementation of HAAR cascades and the KNN algorithm developed specifically to identify the following features with the greatest possible accuracy. Traning datasets included in this sub repo, all datasets hand made.

In, Out: 
Program necessary directories
