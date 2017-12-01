import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { SaleTransactions } from './sale-transactions.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SaleTransactionsService {

    private resourceUrl = SERVER_API_URL + 'api/sale-transactions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sale-transactions';

    constructor(private http: Http) { }

    create(saleTransactions: SaleTransactions): Observable<SaleTransactions> {
        const copy = this.convert(saleTransactions);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(saleTransactions: SaleTransactions): Observable<SaleTransactions> {
        const copy = this.convert(saleTransactions);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SaleTransactions> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to SaleTransactions.
     */
    private convertItemFromServer(json: any): SaleTransactions {
        const entity: SaleTransactions = Object.assign(new SaleTransactions(), json);
        return entity;
    }

    /**
     * Convert a SaleTransactions to a JSON which can be sent to the server.
     */
    private convert(saleTransactions: SaleTransactions): SaleTransactions {
        const copy: SaleTransactions = Object.assign({}, saleTransactions);
        return copy;
    }
}
