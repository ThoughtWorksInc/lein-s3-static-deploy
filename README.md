# s3-static-deploy

A Leiningen plugin to deploy a directory as a static website on s3.

Simply give it your AWS credentials, a bucket name and a directory to deploy and it will
ensure the bucket exists with the correct permissions etc.  It employs [s3-sync](https://github.com/kanej/lein-s3-sync/tree/master/s3-sync)
to ensure that only changed files are uploaded.

This plugin was built to support the use case of creating a static Angular ClojureScript app.

## Configuration

To use lein-s3-static-deploy, you'll need to add a few additional values to your project.clj file.

First, add lein-s3-static-deploy as a plugin:
Put `[s3-static-deploy "0.1.0"]` into the `:plugins` vector.

Put `[s3-static-deploy "0.1.0]` into the `:plugins` vector of your project.clj.

You'll also need to give lein-s3-static-deploy a few instructions so it knows what to do.

lein-s3-static-deploy plays nice with other aws plugins such as [lein-beanstalk](https://github.com/weavejester/lein-beanstalk/blob/master/project.clj) and shares
their configuration block so you don't have to repeat AWS credentials etc..

So inside your `project.clj` do:

`:aws {
        :access-key ~(System/getenv "AWS_ACCESS_KEY")
        :secret-key ~(System/getenv "AWS_SECRET_KEY")
        :s3-static-deploy {:bucket "THE BUCKET YOU WANT TO DEPLOY TO"
                           :local-root "LOCAL DIRECTORY YOU WANT TO DEPLOY FROM"}}`

## Deploy

You should now be able to deploy your website to the AWS using the following command:

$ lein s3-static-deploy

## Thanks
Thanks to [s3-sync](https://github.com/kanej/lein-s3-sync/tree/master/s3-sync) for providing
the functionality to efficiently sync contents to the s3 bucket and [clj-aws-s3](https://github.com/weavejester/clj-aws-s3)
for providing a nice S3 API.

## Contributions welcome
This plugin is very simple and supports our narrow use case.  We would welcome contributions so please fork away!

## License

Copyright © 201 4Thoughtworks Inc

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
