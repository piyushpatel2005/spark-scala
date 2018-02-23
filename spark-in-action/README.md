# Spark in Action

This repo is about practice with Spark in Action.

Download Spark in action box from the link in the [spark-in-action2.json](spark-in-action2.json) file.

Put it in this folder and change the location in [file](spark-in-action-box.json).

Install Vagrant, Oracle VirtualBox and use these commands from this directory.

```shell
vagrant box add manning/spark-in-action
vagrant init manning/spark-in-action
vagrant up
```

Now use SSH to connect to `192.168.10.2` and use `spark` as username and password. Now, the environment is like Hadoop cluster.

Install Eclipse with M2eclipse-scala integration plugin.

Add this url for location and name as m2eclipse-scala.
http://alchim31.free.fr/m2e-scala/update-site
