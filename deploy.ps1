$manifests = @(
  "namespace.yaml",
  "configmap.yaml",
  "secret.yaml",
  "postgres-statefulset.yaml",
  "postgres-service.yaml",
  "backend-deployment.yaml",
  "backend-service.yaml",
  "frontend-deployment.yaml",
  "frontend-service.yaml",
  "ingress.yaml"
)

foreach ($file in $manifests) {
  Write-Host "Applying $file ..."
  kubectl apply -f $file

  if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to apply $file. Exiting script."
    exit $LASTEXITCODE
  }
}
kubectl config set-context --current --namespace=explorandija

Write-Host "All manifests applied successfully!"