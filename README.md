# Spark Scala Tutorial

[Intro to Scala Language](tutorial/scala.md)

[Spark](spark/spark.md)

Use vagrant for making real cluster kind of feel.

When installing various hadoop and spark versions, symbolic links can be used to change the location which Spark version we want to use.

For example, we might have various spark versions in `/opt/` directory and we may create different symbolic links.

```shell
sudo rm -f /usr/local/spark
sudo ln -s /opt/spark-1.6.1-bin-hadoop2.4 /usr/local/spark
```

**Enable SSH connection on Cloudera VM**

Right click on the VirtualMachine and click Settings
Click on General -> Advanced
Set Shared Clipboard to Bidirectional

Click on Network -> Advanced -> Port Forwarding
Add a new entry
Name: 2222
Host Port: 2222
Guest Port: 22