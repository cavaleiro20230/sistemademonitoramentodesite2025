import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { ArrowUpRight, Globe, Users, MapPin, FileText } from "lucide-react"

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col p-6 bg-slate-50">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Sistema de Monitoramento de Site</h1>
        <div className="flex gap-2">
          <Button variant="outline">Configurações</Button>
          <Button>Exportar Relatório</Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total de Visitas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">24,892</div>
            <p className="text-xs text-muted-foreground mt-1">
              <span className="text-green-500">↑ 12%</span> em relação ao período anterior
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Usuários Únicos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">5,673</div>
            <p className="text-xs text-muted-foreground mt-1">
              <span className="text-green-500">↑ 8%</span> em relação ao período anterior
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Tempo Médio</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">3m 42s</div>
            <p className="text-xs text-muted-foreground mt-1">
              <span className="text-red-500">↓ 5%</span> em relação ao período anterior
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Taxa de Rejeição</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">32.4%</div>
            <p className="text-xs text-muted-foreground mt-1">
              <span className="text-green-500">↑ 3%</span> em relação ao período anterior
            </p>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="urls" className="w-full">
        <TabsList className="mb-4">
          <TabsTrigger value="urls">
            <Globe className="h-4 w-4 mr-2" />
            URLs Mais Acessadas
          </TabsTrigger>
          <TabsTrigger value="ips">
            <ArrowUpRight className="h-4 w-4 mr-2" />
            IPs
          </TabsTrigger>
          <TabsTrigger value="users">
            <Users className="h-4 w-4 mr-2" />
            Usuários
          </TabsTrigger>
          <TabsTrigger value="geo">
            <MapPin className="h-4 w-4 mr-2" />
            Geolocalização
          </TabsTrigger>
          <TabsTrigger value="logs">
            <FileText className="h-4 w-4 mr-2" />
            Logs
          </TabsTrigger>
        </TabsList>

        <TabsContent value="urls" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>URLs Mais Acessadas</CardTitle>
              <CardDescription>Lista das páginas mais visitadas no período selecionado</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="rounded-md border">
                <table className="w-full caption-bottom text-sm">
                  <thead className="border-b bg-slate-100">
                    <tr>
                      <th className="h-12 px-4 text-left font-medium">URL</th>
                      <th className="h-12 px-4 text-left font-medium">Acessos</th>
                      <th className="h-12 px-4 text-left font-medium">Tempo Médio</th>
                      <th className="h-12 px-4 text-left font-medium">Taxa de Rejeição</th>
                    </tr>
                  </thead>
                  <tbody>
                    {[
                      { url: "/produtos", acessos: 5842, tempo: "2m 15s", rejeicao: "28%" },
                      { url: "/home", acessos: 4721, tempo: "3m 42s", rejeicao: "22%" },
                      { url: "/contato", acessos: 3654, tempo: "1m 50s", rejeicao: "45%" },
                      { url: "/sobre", acessos: 2987, tempo: "4m 12s", rejeicao: "18%" },
                      { url: "/blog", acessos: 2541, tempo: "5m 30s", rejeicao: "15%" },
                    ].map((item, i) => (
                      <tr key={i} className="border-b">
                        <td className="p-4 align-middle font-medium">{item.url}</td>
                        <td className="p-4 align-middle">{item.acessos}</td>
                        <td className="p-4 align-middle">{item.tempo}</td>
                        <td className="p-4 align-middle">{item.rejeicao}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="ips" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>IPs de Acesso</CardTitle>
              <CardDescription>Endereços IP que acessaram o site</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="rounded-md border">
                <table className="w-full caption-bottom text-sm">
                  <thead className="border-b bg-slate-100">
                    <tr>
                      <th className="h-12 px-4 text-left font-medium">Endereço IP</th>
                      <th className="h-12 px-4 text-left font-medium">Acessos</th>
                      <th className="h-12 px-4 text-left font-medium">Último Acesso</th>
                      <th className="h-12 px-4 text-left font-medium">Localização</th>
                      <th className="h-12 px-4 text-left font-medium">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {[
                      {
                        ip: "192.168.1.105",
                        acessos: 342,
                        ultimo: "Hoje, 14:32",
                        local: "São Paulo, BR",
                        status: "Normal",
                      },
                      {
                        ip: "187.54.23.12",
                        acessos: 215,
                        ultimo: "Hoje, 13:15",
                        local: "Rio de Janeiro, BR",
                        status: "Normal",
                      },
                      {
                        ip: "201.45.78.90",
                        acessos: 189,
                        ultimo: "Hoje, 12:45",
                        local: "Belo Horizonte, BR",
                        status: "Normal",
                      },
                      {
                        ip: "103.24.56.78",
                        acessos: 1254,
                        ultimo: "Hoje, 11:22",
                        local: "Desconhecido",
                        status: "Suspeito",
                      },
                      {
                        ip: "45.67.89.123",
                        acessos: 87,
                        ultimo: "Ontem, 23:10",
                        local: "Porto Alegre, BR",
                        status: "Normal",
                      },
                    ].map((item, i) => (
                      <tr key={i} className="border-b">
                        <td className="p-4 align-middle font-medium">{item.ip}</td>
                        <td className="p-4 align-middle">{item.acessos}</td>
                        <td className="p-4 align-middle">{item.ultimo}</td>
                        <td className="p-4 align-middle">{item.local}</td>
                        <td className="p-4 align-middle">
                          <span
                            className={`px-2 py-1 rounded-full text-xs ${item.status === "Normal" ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800"}`}
                          >
                            {item.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="users" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Usuários Ativos</CardTitle>
              <CardDescription>Usuários autenticados que acessaram o sistema</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="rounded-md border">
                <table className="w-full caption-bottom text-sm">
                  <thead className="border-b bg-slate-100">
                    <tr>
                      <th className="h-12 px-4 text-left font-medium">Usuário</th>
                      <th className="h-12 px-4 text-left font-medium">Sessões</th>
                      <th className="h-12 px-4 text-left font-medium">Última Atividade</th>
                      <th className="h-12 px-4 text-left font-medium">Tempo Total</th>
                      <th className="h-12 px-4 text-left font-medium">Páginas Visitadas</th>
                    </tr>
                  </thead>
                  <tbody>
                    {[
                      { usuario: "joao.silva", sessoes: 24, ultima: "Hoje, 15:10", tempo: "4h 32m", paginas: 87 },
                      { usuario: "maria.santos", sessoes: 18, ultima: "Hoje, 14:45", tempo: "3h 15m", paginas: 65 },
                      { usuario: "carlos.oliveira", sessoes: 12, ultima: "Hoje, 13:22", tempo: "2h 40m", paginas: 43 },
                      { usuario: "ana.pereira", sessoes: 9, ultima: "Ontem, 18:05", tempo: "1h 55m", paginas: 31 },
                      { usuario: "roberto.almeida", sessoes: 7, ultima: "Ontem, 16:30", tempo: "1h 20m", paginas: 25 },
                    ].map((item, i) => (
                      <tr key={i} className="border-b">
                        <td className="p-4 align-middle font-medium">{item.usuario}</td>
                        <td className="p-4 align-middle">{item.sessoes}</td>
                        <td className="p-4 align-middle">{item.ultima}</td>
                        <td className="p-4 align-middle">{item.tempo}</td>
                        <td className="p-4 align-middle">{item.paginas}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="geo" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Geolocalização</CardTitle>
              <CardDescription>Distribuição geográfica dos acessos</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="h-[400px] bg-slate-100 rounded-md flex items-center justify-center mb-4">
                <div className="text-center">
                  <Globe className="h-16 w-16 mx-auto mb-2 text-slate-400" />
                  <p className="text-slate-500">Mapa de distribuição de acessos</p>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <Card>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-sm">Top Países</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <ul className="space-y-2">
                      {[
                        { pais: "Brasil", acessos: 18452 },
                        { pais: "Estados Unidos", acessos: 3254 },
                        { pais: "Portugal", acessos: 1876 },
                        { pais: "Argentina", acessos: 954 },
                        { pais: "Outros", acessos: 356 },
                      ].map((item, i) => (
                        <li key={i} className="flex justify-between items-center">
                          <span>{item.pais}</span>
                          <span className="font-medium">{item.acessos}</span>
                        </li>
                      ))}
                    </ul>
                  </CardContent>
                </Card>

                <Card>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-sm">Top Cidades (Brasil)</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <ul className="space-y-2">
                      {[
                        { cidade: "São Paulo", acessos: 8754 },
                        { cidade: "Rio de Janeiro", acessos: 4532 },
                        { cidade: "Belo Horizonte", acessos: 2143 },
                        { cidade: "Brasília", acessos: 1876 },
                        { cidade: "Outras", acessos: 1147 },
                      ].map((item, i) => (
                        <li key={i} className="flex justify-between items-center">
                          <span>{item.cidade}</span>
                          <span className="font-medium">{item.acessos}</span>
                        </li>
                      ))}
                    </ul>
                  </CardContent>
                </Card>

                <Card>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-sm">Dispositivos</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <ul className="space-y-2">
                      {[
                        { tipo: "Desktop", acessos: 12543 },
                        { tipo: "Mobile", acessos: 9876 },
                        { tipo: "Tablet", acessos: 2473 },
                      ].map((item, i) => (
                        <li key={i} className="flex justify-between items-center">
                          <span>{item.tipo}</span>
                          <span className="font-medium">{item.acessos}</span>
                        </li>
                      ))}
                    </ul>
                  </CardContent>
                </Card>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="logs" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Logs de Acesso</CardTitle>
              <CardDescription>Registros detalhados de acesso ao sistema</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex gap-2 mb-4">
                <Button variant="outline" size="sm">
                  Filtrar
                </Button>
                <Button variant="outline" size="sm">
                  Exportar
                </Button>
                <Button variant="outline" size="sm">
                  Limpar Filtros
                </Button>
              </div>

              <div className="rounded-md border">
                <table className="w-full caption-bottom text-sm">
                  <thead className="border-b bg-slate-100">
                    <tr>
                      <th className="h-12 px-4 text-left font-medium">Data/Hora</th>
                      <th className="h-12 px-4 text-left font-medium">IP</th>
                      <th className="h-12 px-4 text-left font-medium">Usuário</th>
                      <th className="h-12 px-4 text-left font-medium">URL</th>
                      <th className="h-12 px-4 text-left font-medium">Status</th>
                      <th className="h-12 px-4 text-left font-medium">User-Agent</th>
                    </tr>
                  </thead>
                  <tbody>
                    {[
                      {
                        data: "2023-05-21 15:42:32",
                        ip: "192.168.1.105",
                        usuario: "joao.silva",
                        url: "/produtos",
                        status: 200,
                        agent: "Chrome/Windows",
                      },
                      {
                        data: "2023-05-21 15:41:15",
                        ip: "187.54.23.12",
                        usuario: "maria.santos",
                        url: "/home",
                        status: 200,
                        agent: "Firefox/MacOS",
                      },
                      {
                        data: "2023-05-21 15:40:45",
                        ip: "201.45.78.90",
                        usuario: "-",
                        url: "/contato",
                        status: 200,
                        agent: "Safari/iOS",
                      },
                      {
                        data: "2023-05-21 15:39:22",
                        ip: "103.24.56.78",
                        usuario: "-",
                        url: "/admin",
                        status: 403,
                        agent: "Chrome/Linux",
                      },
                      {
                        data: "2023-05-21 15:38:10",
                        ip: "45.67.89.123",
                        usuario: "roberto.almeida",
                        url: "/sobre",
                        status: 200,
                        agent: "Edge/Windows",
                      },
                    ].map((item, i) => (
                      <tr key={i} className="border-b">
                        <td className="p-4 align-middle">{item.data}</td>
                        <td className="p-4 align-middle">{item.ip}</td>
                        <td className="p-4 align-middle">{item.usuario}</td>
                        <td className="p-4 align-middle font-medium">{item.url}</td>
                        <td className="p-4 align-middle">
                          <span
                            className={`px-2 py-1 rounded-full text-xs ${item.status === 200 ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"}`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-4 align-middle text-xs text-slate-500">{item.agent}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </main>
  )
}
