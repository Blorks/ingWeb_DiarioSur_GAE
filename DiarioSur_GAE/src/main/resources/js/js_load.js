/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.load = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=0d8792b0c196edc5ad03a8b89c0bd340&tags=flower&per_page=3&format=json";

            function jsonFlickrApi(rsp) {
                window.rsp = rsp;
                var s = "";
                s = "total number is: " + rsp.photos.photo.length + " ";
                for (var i = 0; i < rsp.photos.photo.length; i++) {
                    photo = rsp.photos.photo[i];
                    t_url = "http://farm" + photo.farm + ".static.flickr.com/" +
                            photo.server + "/" + photo.id + "_" + photo.secret + "_" + "t.jpg";
                    p_url = "http://www.flickr.com/photos/" + photo.owner + "/" + photo.id;

                    s += 'href="' + p_url + '"' + ' alt="' + photo.title +
                            '"src="' + t_url + '"' + '';
                }
                document.writeln(s);
            }