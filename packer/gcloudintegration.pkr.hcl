packer {
  required_plugins {
    googlecompute = {
      version = "~> 1.0"
      source  = "github.com/hashicorp/googlecompute"
    }
  }
}

variable "zone" {
  default = "us-central1-a"
}

variable "project_id" {
  type    = string
  default = "dev-cloud-bhargav"
}

variable "jar_path" {
  type    = string
  default = ""
}

source "googlecompute" "gcp" {
  project_id   = var.project_id
  zone         = var.zone
  source_image = "centos-stream-8-v20240110"
  ssh_username = "uploader"
}

build {
  name = "learn-packer"
  sources = [
    "source.googlecompute.gcp"
  ]

  provisioner "file" {
    source      = var.jar_path
    destination = "/tmp/healthcheck-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "opsagentconfig.yaml"
    destination = "/tmp/opsagentconfig.yaml"
  }

  provisioner "file" {
    source      = "healthzcloud.service"
    destination = "/tmp/healthzcloud.service"
  }

  provisioner "shell" {
    script = "installationscript.sh"
  }
}
