import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { GoodsReceivedFromSupplier } from './goods-received-from-supplier.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GoodsReceivedFromSupplierService {

    private resourceUrl = SERVER_API_URL + 'api/goods-received-from-suppliers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/goods-received-from-suppliers';

    constructor(private http: Http) { }

    create(goodsReceivedFromSupplier: GoodsReceivedFromSupplier): Observable<GoodsReceivedFromSupplier> {
        const copy = this.convert(goodsReceivedFromSupplier);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(goodsReceivedFromSupplier: GoodsReceivedFromSupplier): Observable<GoodsReceivedFromSupplier> {
        const copy = this.convert(goodsReceivedFromSupplier);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<GoodsReceivedFromSupplier> {
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
     * Convert a returned JSON object to GoodsReceivedFromSupplier.
     */
    private convertItemFromServer(json: any): GoodsReceivedFromSupplier {
        const entity: GoodsReceivedFromSupplier = Object.assign(new GoodsReceivedFromSupplier(), json);
        return entity;
    }

    /**
     * Convert a GoodsReceivedFromSupplier to a JSON which can be sent to the server.
     */
    private convert(goodsReceivedFromSupplier: GoodsReceivedFromSupplier): GoodsReceivedFromSupplier {
        const copy: GoodsReceivedFromSupplier = Object.assign({}, goodsReceivedFromSupplier);
        return copy;
    }
}
