# link-shortener

A spring boot application which can encode a full url into a shortened version, and decode back into the full URL.

Encode:
<code>
HTTP POST to "localhost:8080/encode" with body
{
  "url": "https://www.google.com"
}
</code>

Decode:
<code>
HTTP POST to "localhost:8080/decode" with body
{
  "url": "https://tpx.com/7e"
}
</code>

