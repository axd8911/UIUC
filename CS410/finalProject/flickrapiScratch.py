import sys
sys.path
import metapy
exit()
import os
os.environ.get('PATH')
os.environ.get('GITLAB_API_TOKEN')
exit()
import metapy
help(metapy.classify)
import sys
x = open('metaypyclassifyhelp.txt', 'w')
sys.stdout=x
help(metapy.classify)
x.close()
help(metapy.classify)
x = open('metaypyclassifyhelp.txt', 'w')
sys.stdout=x
print "fuck"
print ("fuck")
print("fuck")
a = "fuck"
print(a)
x.close()
x = open('metaypyclassifyhelp.txt', 'w')
sys.stdout=x
print(help(metapy.classify))
dir(hel)
dir(help)
dir(help())
x.close()
x = open('metaypyclassifyhelp.txt', 'w')
sys.stdout=x
import pydoc
pydoc.help(metapy.classify)
x.close()
pydoc.render_doc(metapy.classify)
x = open('metaypyclassifyhelp.txt', 'w')
sys.stdout=x
pydoc.render_doc(metapy.classify)
x.close()
x = open(r'metaypyclassifyhelp.txt', 'w')
sys.stdout=x
quit()
import flickrapi
exit()
import flickrapi
api_key =  "5326aa343287b8523fb1542d2a7afad0"
api_secret = "7e85b10f2639faf9"
flickr = flickrapi.FlickrAPI(api_key, api_secret)
searchResults = flickr.photos_search(tag='dam', has_geo=True)
for p in searchResults.photos[0].photo: print(photo['title'])
for p in searchResults[0]:
	print (p.photo['title'])
	print (p['title'])
for p in searchResults[0]:
	print (p['title'])
photos = searchResults['photos']
del(flickr)
flickr = flickrapi.FlickrAPI(api_key, api_secret, format='parsed-json')
searchResults = flickr.photos.search(tag="dam", has_geo=True)
photos = searchResults['photos']
from pprint import pprint
pprint(photos)
searchResults = flickr.photos.search(tags="dam", has_geo=True)
photos = searchResults['photos']
pprint(photos)
searchResults = flickr.photos.search(tags="dam", has_geo=True, extras="geo, tags")
photos = searchResults['photos']
pprint(photos)
type(photos)
photos['total']
photos.keys()
photos['photo']
photos.keys()
photos['page']
photos['pages']
photos['perpage']
len(photos['photo'])
searchResults.keys()
len(searchResults['photos'])
searchResults = flickr.photos.search(tags="dam", has_geo=True, page=2, extras="geo, tags")
searchResults['photos']['page']
type(searchResults['photos']['photo'])
len(searchResults['photos']['photo'])
searchResults['photos']['photo'][0]
searchResults = flickr.photos.search(tags="dam", has_geo=True, page=1, extras="geo, tags, owner_name, url_o")
searchResults['photos']['photo'][0]
ex = flickr.photos.getInfo(photo_id='14889337026')
ex
pprint(ex)
import readline
readline.write_history_file('/home/nathan/Dropbox/School/UIUC/CS410/finalProject/flickrapiScratch.py')
